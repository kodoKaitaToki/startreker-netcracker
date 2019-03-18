import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { PossibleServiceService } from 'src/app/services/possible-service.service';
import { TreeNode, MessageService } from 'primeng/api';
import { PossibleService } from '../shared/model/possible-service';
import { HttpErrorResponse } from '@angular/common/http';
import { Trip } from '../shared/model/trip';
import { Suggestion } from '../shared/model/suggestion';

@Component({
  selector: 'app-suggestions-tree',
  templateUrl: './suggestions-tree.component.html',
  styleUrls: ['./suggestions-tree.component.scss']
})
export class SuggestionsTreeComponent implements OnInit {
  suggestion: Suggestion;

  @Input() trip: Trip;

  inputTree: TreeNode[];

  selectedTree: TreeNode[];

  loading: boolean;

  @Output() selectedSuggestion = new EventEmitter<Suggestion>();

  @Output() canceledSuggestion = new EventEmitter<any>();

  //@Output() selectedServices = new EventEmitter<Service[]>();

  constructor(
    private possibleServiceApi: PossibleServiceService,
    private messageService: MessageService) {
  }

  ngOnInit() {
    this.getTicketClasses(this.trip);
  }

  getTicketClasses(trip: Trip) {
    this.inputTree = [];
    this.inputTree = this.inputTree.concat(
      trip.ticket_classes.map(ticketClass => {
        let node: TreeNode = {
          label: ticketClass.class_name,
          data: ticketClass,
          icon: "fa fa-ticket",
          selectable: false,
          type: "ticketClass",
          leaf: false,
        }
        return node;
    }));
  }

  loadNode(event: { node: TreeNode; }) {
    let nullChecks:boolean = (this.selectedTree !== undefined 
      && event.node.children !== undefined);

    if(event.node) {
      if (nullChecks
        && event.node.children.some(node => this.selectedTree.includes(node))) {
        this.showMessage(this.createMessage('warn', `Nodes still selected`, 'Nodes will not load if any are selected'));
        return;
      }
      switch(event.node.type) {
        case "ticketClass": {
          this.populateTicketClass(event.node);
          //this.bundleUpdate();
          break;
        }
      }
    }
  }

  populateTicketClass(ticketClass: TreeNode) {
    this.possibleServiceApi.getAll(ticketClass.data.class_id)
    .subscribe(services => {
      ticketClass.children = services.map((service: PossibleService) => {
          let node: TreeNode = {
            label: service.service_name,
            data: service,
            type: "service",
            icon: "fa fa-list-alt",
            selectable: true,
            leaf: true
          }
          return node;
      });
      this.selectServices(ticketClass);
    }, (error: HttpErrorResponse) => {
      this.showMessage(this.createMessage('error', `Error message - ${error.error.status}`, error.error.message));
    });
  }

  nodeSelect(event: { node: TreeNode; }) {
    let falseNodes: TreeNode[] = this.inputTree.filter(filterNode => {
      let childrenExist = (filterNode.children !== undefined);
      let isTicketClass = (childrenExist && filterNode.children.length > 0);
      return (isTicketClass && !filterNode.children.includes(event.node));
    });

    falseNodes.forEach(node => {
      node.children.forEach(child => {child.selectable = false})
    });
  }

  nodeUnselect(event: { node: TreeNode; }) {
    if (this.selectedTree.length == 0) {
      this.setNodesSelectable();
    }
  }

  setNodesSelectable() {
    let parentNodes: TreeNode[] = this.inputTree.filter(filterNode => {
      let isTicketClass = (filterNode.children !== undefined && filterNode.children.length > 0);
      return (isTicketClass);
    });

    parentNodes.forEach(node => {
      node.children.forEach(child => {child.selectable = true})
    });
  }

  recieveSuggestion(suggestion: Suggestion) {
    this.suggestion = suggestion;
    console.log(this.suggestion);
    let ticketNode = this.inputTree.find(node => {
      return node.data == suggestion.ticketClass;
    });
    ticketNode.expanded = true;
    this.populateTicketClass(ticketNode);
  }

  addSuggestion() {
    let id = this.suggestion === undefined ? null :  this.suggestion.id;
    let outputSuggestion = new Suggestion(
      id,
      this.selectedTree.map(node => {
        return node.data;
      }),
      this.selectedTree[0].parent.data
    );
    this.selectedSuggestion.emit(outputSuggestion);
    this.clearSelectedTree();
  }

  selectServices(ticketNode) {
    this.clearSelectedTree();
    ticketNode.children.forEach(node => {
      if (this.suggestion.services.some(service => {
          return node.data.id == service.id
        })) {
        this.selectedTree.push(node);
      }
    });
  }

  clearSelectedTree() {
    this.selectedTree !== undefined ? this.selectedTree.length = 0 : this.selectedTree = [];
  }

  cancelSuggestion() {
    this.canceledSuggestion.emit();
  }

  createMessage(severity: string, summary: string, detail: string): any {
    return {
      severity: severity,
      summary: summary,
      detail: detail
    };
  }

  showMessage(msgObj: any) {
    this.messageService.add(msgObj);
  }

}
