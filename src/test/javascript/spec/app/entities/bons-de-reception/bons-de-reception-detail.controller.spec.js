'use strict';

describe('Controller Tests', function() {

    describe('Bons_de_Reception Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBons_de_Reception, MockFournisseur, MockCondition_de_reglement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBons_de_Reception = jasmine.createSpy('MockBons_de_Reception');
            MockFournisseur = jasmine.createSpy('MockFournisseur');
            MockCondition_de_reglement = jasmine.createSpy('MockCondition_de_reglement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bons_de_Reception': MockBons_de_Reception,
                'Fournisseur': MockFournisseur,
                'Condition_de_reglement': MockCondition_de_reglement
            };
            createController = function() {
                $injector.get('$controller')("Bons_de_ReceptionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:bons_de_ReceptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
